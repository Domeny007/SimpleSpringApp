<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@tag description="Default Layout Tag" pageEncoding="UTF-8"%>

<%@attribute name="selVal" %>

<%@attribute name="secVal" %>

<%@attribute name="thirdVal" %>

<option selected value="${selVal}">${selVal}</option>
<option value="${secVal}">${secVal}</option>

<c:if test="${not empty thirdVal}">
    <option value="${thirdVal}">${thirdVal}</option>
</c:if>
