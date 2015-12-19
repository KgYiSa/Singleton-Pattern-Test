<%--
  Created by IntelliJ IDEA.
  User: GuoXiaowu
  Date: 2015/11/23
  Time: 16:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<body>
<form method="POST" enctype="multipart/form-data"
      action="/upload">
    File to upload: <input type="file" name="file"><br /> Name: <input
        type="text" name="name"><br /> <br /> <input type="submit"
                                                     value="Upload"> Press here to upload the file!
</form>
</body>
</html>
