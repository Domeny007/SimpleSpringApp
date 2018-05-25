<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@tag description="Default Layout Tag" pageEncoding="UTF-8"%>

<%@attribute name="actionPath" %>

<%@attribute name="btnValue" %>


<form action="<c:url value="${actionPath}"/>" >
    <input type="submit" value="${btnValue}" class="button">
</form>



