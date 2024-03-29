<#-- @ftlvariable name="request" type="it.barusu.paymen.channel.process.bean.TransactionRequest" -->
<#-- @ftlvariable name="IdUtils" type="it.barusu.paymen.util.IdUtils" -->
<#setting number_format="######0" />
<#import "WECHAT_Macro.ftl" as macro>
<@macro.compress_single_line>
<?xml version="1.0" encoding="UTF-8"?>
<xml>
    <appid>${request.secret.appId}</appid>
    <mch_id>${request.secret.merchantNo}</mch_id>
    <nonce_str>${IdUtils.uuidWithoutDash()}</nonce_str>
    <out_trade_no>${request.transaction.transactionNo}</out_trade_no>
    <sign></sign>
</xml>
</@macro.compress_single_line>