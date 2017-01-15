<c:if test="${not empty ActionInfo.message}">
    <div id="infopan" name="infopan" class="alert alert-info alert-block" style="margin-bottom: 0px">
        <h4 class="alert-heading">${ActionInfo.message}</h4>
    </div>
</c:if>