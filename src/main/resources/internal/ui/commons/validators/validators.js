"use strict";
export default {
  validateRequired: (value) => {
    return !!value || 'Field cannot be empty';
  },
  validateUppercase: (value) => {
    const pattern = /^[A-Z]+$/;
    return pattern.test(value) || 'Only uppercase letters (A-Z) are allowed';
  },
  validateName: (value) => {
    const pattern = /^[a-zA-Z0-9 \-_+\.\*\#\$@]+$/;
    return pattern.test(value) || 'Only letters and spaces are allowed';
  }
}