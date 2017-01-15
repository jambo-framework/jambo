jQuery.extend(jQuery.validator.messages, {
    required: msgInputNotNull,
    remote: msgInput,
    email: msgInputCorrectEmail,
    url: "<fmt:message key='msgValidate_url'/>",
    date: msgInvalidDate,
    dateISO: msgInvalidTime+"(ISO).",
    number: msgInvalidNumberFormat,
    digits: msgInvalidNumberFormat,
    creditcard: "<fmt:message key='msgValidate_creditcard'/>",
    equalTo: "<fmt:message key='msgValidate_equalTo'/>",
    accept: "<fmt:message key='msgValidate_accept'/>",
    maxlength: jQuery.validator.format("<fmt:message key='msgValidate_maxlength'/>"),
    minlength: jQuery.validator.format("<fmt:message key='msgValidate_minlength'/>"),
    rangelength: jQuery.validator.format("<fmt:message key='msgValidate_rangelength'/>"),
    range: jQuery.validator.format("<fmt:message key='msgValidate_range'/>"),
    max: jQuery.validator.format("<fmt:message key='msgValidate_max'/>"),
    min: jQuery.validator.format("<fmt:message key='msgValidate_min'/>")
});
