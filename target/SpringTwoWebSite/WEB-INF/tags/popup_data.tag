<%@ tag import="utils.Const" %><%--
  Created by IntelliJ IDEA.
  User: Ruslan
  Date: 18.11.2017
  Time: 15:22
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@tag description="Default Layout Tag" pageEncoding="UTF-8"%>

<%@attribute name="data"%>

<c:if test="${data != null && !data.trim().equals('')}">
    <div id = "popup_data">
        ${data}
        <% session.removeAttribute(Const.POPUP_DATA); %>
    </div>
</c:if>
