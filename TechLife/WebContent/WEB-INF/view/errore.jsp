<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>TechLife - Si è verificato un errore</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <div class="error-page-container">
        <div class="error-wrapper">
            <div class="error-card">
                <%
                    Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
                    if (statusCode == null) {
                        statusCode = response.getStatus();
                    }
                %>
                
                <h1 class="error-code-title"><%= (statusCode != null) ? statusCode : "Errore" %></h1>
                
                <h2 class="error-subtitle">Operazione non riuscita</h2>
                
                <p class="error-description">
					Scusaci ma c'è stato un imprevisto
                </p>
                
                <div class="error-actions">
                    <a href="${pageContext.request.contextPath}/login.jsp">
                        <button type="button" class="btn-register">
                            Torna al Login
                        </button>
                    </a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>