<%-- 
    Document   : time.jsp
    Created on : Nov 25, 2019, 3:54:32 PM
    Author     : hoang-trung-duc
--%>

<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" href="css/bulma.min.css">
        <link rel="stylesheet" href="css/style.css">
        <title>Set up system time</title>
    </head>
    <body>
        <div class="main columns">
            <div class="column is-6 is-offset-3">
                <input size="16" type="text" readonly
                       class="form_datetime input is-rounded is-primary" id="time">
                <button class="button is-primary" onclick="setTime()">
                    SET
                </button>
            </div>

        </div>
    </body>
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript">
        $(".form_datetime").datetimepicker({format: 'yyyy-mm-dd hh:ii'});
    </script>

    <script src="js/time.js"></script>
</html>
