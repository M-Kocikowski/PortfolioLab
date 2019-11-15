document.addEventListener("DOMContentLoaded", function () {

    /**
     * Form Select
     */
    class FormSelect {
        constructor($el) {
            this.$el = $el;
            this.options = [...$el.children];
            this.init();
        }

        init() {
            this.createElements();
            this.addEvents();
            this.$el.parentElement.removeChild(this.$el);
        }

        createElements() {
            // Input for value
            this.valueInput = document.createElement("input");
            this.valueInput.type = "text";
            this.valueInput.name = this.$el.name;

            // Dropdown container
            this.dropdown = document.createElement("div");
            this.dropdown.classList.add("dropdown");

            // List container
            this.ul = document.createElement("ul");

            // All list options
            this.options.forEach((el, i) => {
                const li = document.createElement("li");
                li.dataset.value = el.value;
                li.innerText = el.innerText;

                if (i === 0) {
                    // First clickable option
                    this.current = document.createElement("div");
                    this.current.innerText = el.innerText;
                    this.dropdown.appendChild(this.current);
                    this.valueInput.value = el.value;
                    li.classList.add("selected");
                }

                this.ul.appendChild(li);
            });

            this.dropdown.appendChild(this.ul);
            this.dropdown.appendChild(this.valueInput);
            this.$el.parentElement.appendChild(this.dropdown);
        }

        addEvents() {
            this.dropdown.addEventListener("click", e => {
                const target = e.target;
                this.dropdown.classList.toggle("selecting");

                // Save new value only when clicked on li
                if (target.tagName === "LI") {
                    this.valueInput.value = target.dataset.value;
                    this.current.innerText = target.innerText;
                }
            });
        }
    }

    document.querySelectorAll(".form-group--dropdown select").forEach(el => {
        new FormSelect(el);
    });

    /**
     * Hide elements when clicked on document
     */
    document.addEventListener("click", function (e) {
        const target = e.target;
        const tagName = target.tagName;

        if (target.classList.contains("dropdown")) return false;

        if (tagName === "LI" && target.parentElement.parentElement.classList.contains("dropdown")) {
            return false;
        }

        if (tagName === "DIV" && target.parentElement.classList.contains("dropdown")) {
            return false;
        }

        document.querySelectorAll(".form-group--dropdown .dropdown").forEach(el => {
            el.classList.remove("selecting");
        });
    });

    /**
     * Switching between form steps
     */
    class FormSteps {
        constructor(form) {
            this.$form = form;
            this.$next = form.querySelectorAll(".next-step");
            this.$prev = form.querySelectorAll(".prev-step");
            this.$step = form.querySelector(".form--steps-counter span");
            this.$donation = {
                quantity: Number,
                categories: [],
                institution: {
                    id: Number,
                    name: String,
                    description: String
                },
                street: String,
                city: String,
                zipCode: String,
                pickUpDate: String,
                pickUpTime: String,
                pickUpComments: String
            };
            this.currentStep = 1;

            this.$stepInstructions = form.querySelectorAll(".form--steps-instructions p");
            const $stepForms = form.querySelectorAll("form > div");
            this.slides = [...this.$stepInstructions, ...$stepForms];

            $.get({
                url: 'http://localhost:8080/donation/categories'
            }).done(result => {
                let $divStep1 = $('#form--categories>div').first();
                for (let i = 0; i < result.length; i++) {
                    let $div = $(`
                        <div class="form-group form-group--checkbox">
                            <label>
                                <input type="checkbox" name="categories" value="${result[i].id}"/>
                                <span class="checkbox"></span>
                                <span class="description">${result[i].name}</span>
                            </label>
                        </div>
                    `);
                    $div.insertBefore($divStep1.find('.form-group--buttons'));
                }
            });

            this.init();
        }

        /**
         * Init all methods
         */
        init() {
            this.events();
            this.updateForm();
        }

        /**
         * All events that are happening in form
         */
        events() {
            // Next step
            this.$next.forEach(btn => {
                btn.addEventListener("click", e => {
                    e.preventDefault();
                    this.currentStep++;
                    if (this.currentStep === 3) {
                        $.get({
                            url: 'http://localhost:8080/donation/institutions'
                        }).done(result => {
                            let $divStep3 = $('div[data-step="3"]');
                            for (let i = 0; i < result.length; i++) {
                                let $div = $(`
                                              <div class="form-group form-group--checkbox">
                                                <label>
                                                    <input type="radio" name="organization" value="${result[i].id}" />
                                                    <span class="checkbox radio"></span>
                                                    <span class="description">
                                                        <div class="title">${result[i].name}</div>
                                                        <div class="subtitle">${result[i].description}</div>
                                                    </span>
                                                </label>
                                              </div>
                                `);
                                $div.insertBefore($divStep3.find('.form-group--buttons'));
                            }
                        });
                    }
                    this.updateForm(true);
                });
            });

            // Previous step
            this.$prev.forEach(btn => {
                btn.addEventListener("click", e => {
                    e.preventDefault();
                    this.currentStep--;
                    this.updateForm(false);
                });
            });

            // Form submit
            this.$form.querySelector("form").addEventListener("submit", e => this.submit(e));
        }

        /**
         * Update form front-end
         * Show next or previous section etc.
         */
        updateForm(increase) {
            this.$step.innerText = this.currentStep;

            if (increase) {
                switch (this.currentStep - 1) {
                    case 1:
                        const $formCheckboxes = $('.form-group--checkbox input:checked');
                        const $descriptions = $formCheckboxes.siblings('.description');
                        const arrayLength = this.$donation.categories.length;
                        this.$donation.categories.splice(0, arrayLength);

                        for (let i = 0; i < $formCheckboxes.length; i++) {
                            this.$donation.categories.push({
                                id: $formCheckboxes.get(i).value,
                                name: $descriptions.get(i).innerText
                            });
                        }
                        break;

                    case 2:
                        this.$donation.quantity = $('.form-group--inline input[name="bags"]').val();
                        break;

                    case 3:
                        const $checkedRadio = $('input[type="radio"]:checked');
                        this.$donation.institution.id = $checkedRadio.val();
                        this.$donation.institution.name = $checkedRadio.siblings().find('.title').text();
                        this.$donation.institution.description = $checkedRadio.siblings().find('.subtitle').text();
                        break;

                    case 4:
                        this.$donation.street = $('input[name="address"]').val();
                        this.$donation.city = $('input[name="city"]').val();
                        this.$donation.zipCode = $('input[name="postcode"]').val();
                        this.$donation.pickUpDate = $('input[name="data"]').val();
                        this.$donation.pickUpTime = $('input[name="time"]').val();
                        this.$donation.pickUpComments = $('textarea[name="more_info"]').val();
                        break;

                    default:
                        break;
                }
            }

            // TODO: Validation

            this.slides.forEach(slide => {
                slide.classList.remove("active");

                if (slide.dataset.step == this.currentStep) {
                    slide.classList.add("active");
                }
            });

            this.$stepInstructions[0].parentElement.parentElement.hidden = this.currentStep >= 5;
            this.$step.parentElement.hidden = this.currentStep >= 5;

            // TODO: get data from inputs and show them in summary
        }

    }

    const form = document.querySelector(".form--steps");
    if (form !== null) {
        new FormSteps(form);
    }
});
