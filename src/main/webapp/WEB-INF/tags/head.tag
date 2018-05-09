<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@tag description="Default Layout Tag" pageEncoding="UTF-8"%>

<%@attribute name="title" %>

<%@attribute name="cssFilePath" %>

<%@attribute name="scriptFilePath" %>

<head>

    <title>${title}</title>

    <c:if test="${not empty cssFilePath}">
        <link rel="stylesheet" type="text/css" href="<c:url value = "${cssFilePath}"/>">
    </c:if>

    <c:if test="${not empty scriptFilePath}">
        <script src = "<c:url value = "${scriptFilePath}"/>" async></script>
    </c:if>

</head>


