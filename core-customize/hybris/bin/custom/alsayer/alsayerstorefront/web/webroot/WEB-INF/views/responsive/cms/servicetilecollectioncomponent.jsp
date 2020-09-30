<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<c:if test="${component.visible}">
       <ul class="servicetile">
           <c:forEach items="${components}" var="component">
               <c:if test="${component.visible}">
                   <cms:component component="${component}" evaluateRestriction="true" class="servicetile--primary" element="li"/>
               </c:if>
           </c:forEach>
       </ul>
</c:if>

