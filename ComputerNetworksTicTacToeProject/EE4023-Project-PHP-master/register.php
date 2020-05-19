<!DOCTYPE html>
<html lang="en-ie">

<head>
    <title>Register</title>
    <style>
        *{
            font-family: "Century Gothic", CenturyGothic, Geneva, AppleGothic, sans-serif;
        }
        body {
            background-color: lightblue;
        }
        .center {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }
        .button {
            background-color: #4CAF50; /* Green */
            width: 130px;
            border: none;
            color: white;
            padding: 16px 32px;
            align-self: center;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            margin: 4px 2px;
            -webkit-transition-duration: 0.4s; /* Safari */
            transition-duration: 0.4s;
            cursor: pointer;
        }
        #registerBtn {
            background-color: white; 
            color: black; 
            border: 2px solid #4CAF50;
        }
        #registerBtn:hover {
            background-color: #4CAF50;
            color: white;
        }
        #backBtn {
            background-color: white; 
            color: black; 
            border: 2px solid #f44336;
        }
        #backBtn:hover {
            background-color: #f44336;
            color: white;
        }
        h1 {
            color: black;
            text-align: center;
        }
        input[type=text], input[type=password]{
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    <script>
        $.ajaxSetup({
                cache: false
        });

        $(document).ready(function(){

            $(document).on('click', '#registerBtn', function()
            {
                val1 = $("#username_text").val();
                val2 = $("#password_text").val();
                val3 = $("#name_text").val();
                val4 = $("#surname_text").val();

                $.post("tryRegister.php",
                    {
                        uname: val1,
                        pword: val2,
                        name: val3,
                        surname: val4
                    },
                    function(data, status)
                    {
                        $("#results").html(data);
                    }
                );

            });

        });

        $(document).on('click', '#backBtn', function()
        {
            window.location("index.php");
        });

    </script>
	
</head>
<body>
<div id="container">
    <div id="header">
    </div>
    <div id="content" class="center">
        <form>
            <table>
                <tr>
                    <td>
                        <label for="username_text">Username:</label>
                        <input id="username_text" type="text" name="uname" id="uname" tabindex="1" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="password_text">Password:</label>
                        <input id="password_text" type="password" name="pword" id="pword" tabindex="2" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="name_text">Name:</label>
                        <input id="name_text" type="text" name="name" id="name" tabindex="3" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="surname_text">Surname:</label>
                        <input id="surname_text" type="text" name="surname" id="surname" tabindex="4" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <input id="registerBtn" type="button" class="button" name="register" value="Register" tabindex="5" /> 
                        <input id="backBtn" type="button" class="button" name="back" value="Back" tabindex="6"/>
                    </td>
                </tr>
            </table>
        </form>
        <div id="results"></div>
    </div>
</div>

</body>

</html>