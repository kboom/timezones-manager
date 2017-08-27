export class ValidationRule {

    constructor(
        public readonly rule: Function,
        public readonly code: string
    ) {}

    matches = (value) => this.rule(value);

    static ruleOf(rule: any, code: string) {
        return new ValidationRule(rule, code)
    }

}

export const PASSWORD_REGEX = ValidationRule.ruleOf((value) => value.match(/^(?=.*[0-9])[a-zA-Z0-9!@#$%^&*]{6,100}$/), "password.regex");
export const USERNAME_REGEX = ValidationRule.ruleOf((value) => value.match(/^[a-zA-Z0-9]*$/), "username.regex");
export const EMAIL_REGEX = ValidationRule.ruleOf((value) => value.match(/[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/), "email.regex");