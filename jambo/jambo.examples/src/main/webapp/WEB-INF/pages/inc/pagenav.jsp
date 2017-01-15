<div class="box-foot">
        <strong id="nav_total_page" <c:if test="${dp.totalPage eq 0}">hidden</c:if>>
        <fmt:message key='button_total_page'/>
        <code>${dp.totalPage}</code>
        <fmt:message key="button_page" />
    </strong>
    &nbsp;
    <strong><fmt:message key="button_current_page" />
        <code id="nav_pageno" name="mav_pageno">${dp.pageNo}</code>
        <fmt:message key="button_page" />
    </strong>
    <div class="actions">
        <ul>
            <li id="nav_first_pan" <c:if test="${dp.isFirst}">hidden</c:if>>
                <a href="javascript:showFirstPage('<%=ajaxurl%>', 'tableid');" class="btn btn-square tip" >
                    <fmt:message key='button_first_page'/></a>
            </li>
            <li id="nav_previsous_pan" <c:if test="${dp.pageNo eq 1}">hidden</c:if>>
                <a href="javascript:showPreviousPage('<%=ajaxurl%>', 'tableid');" class="btn btn-square tip">
                    <fmt:message key='button_forward'/></a>
            </li>
            <li id="nav_next_pan" <c:if test="${dp.isLast}">hidden</c:if>>
                <a href="javascript:showNextPage('<%=ajaxurl%>', 'tableid');" class="btn btn-square tip">
                    <fmt:message key='button_next'/></a>
            </li>
            <li id="nav_last_pan" <c:if test="${dp.pageNo >= dp.totalPage}">hidden</c:if>>
                <a href="javascript:showLastPage('<%=ajaxurl%>', 'tableid');" class="btn btn-square tip">
                    <fmt:message key='button_last_page'/></a>
            </li>
        </ul>
    </div>
</div>