<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>File Upload</title>
</head>
<body>

<form action="/home" method="post" enctype="multipart/form-data">
    Select File to Upload:
    <input type="file" name="fileToUpload">
    <br>
    <input type="submit" value="Upload">
</form>

<a href="/home/btn">BTN</a>
<a href="/home/sum">SUM</a>

</body>
</html>