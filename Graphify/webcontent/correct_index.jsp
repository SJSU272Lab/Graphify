
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%String baseUrl = getServletContext().getInitParameter("BaseUrl");%>
<!DOCTYPE html>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Simple jQuery file upload</title>
    <script src="//code.jquery.com/jquery-2.1.4.min.js"></script>

    <script type="text/javascript">

        var isJpg = function(name) {
            return name.match(/jpg$/i)
        };

        var isPng = function(name) {
            return name.match(/png$/i)
        };

        var isZip = function(name) {
            return name.match(/zip$/i)
        };

        $(document).ready(function() {
            var file = $('[name="file"]');
            var imgContainer = $('#imgContainer');

            $('#btnUpload').on('click', function() {
                var filename = $.trim(file.val());

                if (!(isJpg(filename) || isPng(filename) || isZip(filename))) {
                    alert('Please browse a JPG/PNG/ZIP file to upload ...');
                    return;
                }

                $.ajax({
                    url: '<%=baseUrl%>api/echofile',
                    type: "POST",
                    data: new FormData(document.getElementById("fileForm")),
                    enctype: 'multipart/form-data',
                    processData: false,
                    contentType: false
                }).done(function(data) {
                    imgContainer.html('');
                    var result = "<h1" + " is valid "+ data.valid + " message: " +data.message + "/>";

                    imgContainer.append(result);
                }).fail(function(jqXHR, textStatus) {
                    //alert(jqXHR.responseText);
                    alert('File upload failed ...');
                });

            });

            $('#btnClear').on('click', function() {
                imgContainer.html('');
                file.val('');
            });
        });

    </script>
</head>
<body style="font-family: calibri; font-size: 8pt">
<div>
    <form id="fileForm">
        <input type="file" name="file" />
        <input type="text" name="host">
        <input type="text" name="port">
        <input type="text" name="user">
        <input type="text" name="password">
        <input type="text" name="db">
        <button id="btnUpload" type="button">Upload file</button>
        <button id="btnClear" type="button">Clear</button>
    </form>
    <div id="imgContainer"></div>
</div>
</body>
</html>