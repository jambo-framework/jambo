<#if dp??>
    <div class="box-foot">
        <strong id="nav_total_page" <#if dp.totalPage == 0>hidden</#if>>
            <@message key='button_total_page'/>
            <code>${dp.totalPage}</code>
            <@message key="button_page" />
        </strong>
        &nbsp;
        <strong><@message key="button_current_page" />
            <code id="nav_pageno" name="mav_pageno">${dp.pageNo}</code>
            <@message key="button_page" />
        </strong>
        <div class="actions">
            <ul>
                <li id="nav_first_pan" <#if dp.isFirst>hidden</#if>>
                    <a href="javascript:showFirstPage('${ajaxurl}', 'tableid');" class="btn btn-default tip" >
                        <@message key='button_first_page'/></a>
                </li>
                <li id="nav_previsous_pan" <#if dp.pageNo == 1>hidden</#if>>
                    <a href="javascript:showPreviousPage('${ajaxurl}', 'tableid');" class="btn btn-default tip">
                        <@message key='button_forward'/></a>
                </li>
                <li id="nav_next_pan" <#if dp.isLast>hidden</#if>>
                    <a href="javascript:showNextPage('${ajaxurl}', 'tableid');" class="btn btn-default tip">
                        <@message key='button_next'/></a>
                </li>
                <li id="nav_last_pan" <#if dp.pageNo gte dp.totalPage>hidden</#if>>
                    <a href="javascript:showLastPage('${ajaxurl}', 'tableid');" class="btn btn-default tip">
                        <@message key='button_last_page'/></a>
                </li>
            </ul>
        </div>
    </div>
</#if>
