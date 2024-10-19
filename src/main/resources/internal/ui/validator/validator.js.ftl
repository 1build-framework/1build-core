"use strict";
export default {
<#list validators as validator>
  ${validator.name}: (value) => {
    const pattern = ${validator.regex};
    return pattern.test(value) || '${validator.message}';
  }
  <#if validator_has_next>,</#if>
</#list>
};