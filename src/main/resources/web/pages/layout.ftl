<#macro page>
<!DOCTYPE HTML>
    <head>
        <title>Free Movies Store Website Template | Home :: w3layouts</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
        <link href="/css/style.css" rel="stylesheet" type="text/css" media="all"/>
        <script type="text/javascript" src="/js/jquery-1.9.0.min.js"></script> 
        <script type="text/javascript" src="/js/move-top.js"></script>
        <script type="text/javascript" src="/js/easing.js"></script>
    </head>
    <body>
        <div class="header">
            <div class="headertop_desc">
                <div class="wrap">
                    <div class="account_desc">
                        <ul>
                            <li><a href="register">Register</a></li>
                            <li><a href="login">Login</a></li>
                        </ul>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
            <div class="wrap">
                <div class="header_top">
                    <div class="logo">
                        <a href="index.html"><img src="/images/logo.png" alt="" /></a>
                    </div>
                    <div class="header_top_right">
                        <div class="search_box">
                            <form>
                                <input type="text" value="Search" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Search';}"><input type="submit" value="">
                            </form>
                        </div>
                        <div class="clear"></div>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        </div>

        <div class="main">
            <div class="wrap">
                <#nested>
        	</div>
        </div>

        <script type="text/javascript">
            $(document).ready(function() {
                $().UItoTop({ easingType: 'easeOutQuart' });
            });
        </script>
        <a href="#" id="toTop"><span id="toTopHover"> </span></a>
    </body>
</html>
</#macro>