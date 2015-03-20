<#macro page>
<!DOCTYPE HTML>
    <head>
        <title>Movies recommender system - Like Movie | Home :: w3layouts</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
        <link href="/css/style.css" rel="stylesheet" type="text/css" media="all"/>
        <script type="text/javascript" src="/js/jquery-1.9.0.min.js"></script>
        <script type="text/javascript" src="/js/jquery.autocomplete.min.js"></script> 
        <script type="text/javascript" src="/js/move-top.js"></script>
        <script type="text/javascript" src="/js/easing.js"></script>
    </head>
    <body>
        <div class="header">
            <div class="headertop_desc">
                <div class="wrap">
                    <div class = "nav_list">
                        <ul>
                            <li><a href="/">Hello, ${username!"guest" }</a></li>
                        </ul>
                    </div>
                    <div class="account_desc">
					  <ul>
					  <#if !username?has_content>
					    <li id="login">
					      <a class="login-trigger" tabindex="0">Log in</a>
					      <div class="login-form">
					      	  <div class="login-form-left-pane">
					            <form action="/login" method="post">
					                <input type="text" name="username" placeholder="Username" />
					                <input type="password" name="password" placeholder="Password" />

					                <input type="submit" class="login-button" value="Log In" />
					            </form>
					            </div>
					            <div class="login-form-right-pane">
					            	<a href="${facebookAuthUrl}"><img src="/images/facebook-login.png" alt="Login with facebook" /></a>
					            </div>
					            <div class="clear"></div>
					        </div>                 
					    </li>
					    <li id="signup">
					      <a class="login-trigger" tabindex="0">Sign up</a>
					      <div class="login-form" id="reg-form">
				            <form action="/register" method="post">
				                <input type="text" name="username" placeholder="Username" />
				                <input type="password" name="password" placeholder="Password" />
				                <input type="password" name="repeat_password" placeholder="Repeat password">

				                <input type="submit" class="login-button" value="Sign up" />
				            </form>
					        </div>
					    </li>
                        <#else>
                            <li><a href="/logout">Logout</a></li>
                        </#if>
					  </ul>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
            <div class="wrap">
                <div class="header_top">
                    <div class="logo">
                        <a href="/"><img src="/images/logo.png" alt="MRecommend" /></a>
                    </div>
                    <div class="header_top_right">
                        <div class="search_box">
                            <form>
                                <input id="movies-search" type="text" /><input type="submit" value=""/>
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

                $('#movies-search').autocomplete({
				    serviceUrl: '/movies',
				    paramName: 'name',
				    transformResult: function(response) {
				        return {
				            suggestions: $.map(JSON.parse(response), function(dataItem) {
				                return { value: dataItem.title, data: (dataItem.id).toString() };
				            })
				        };
				    },
				    onSelect: function(suggestion) {
				    	window.location = "/movies/" + suggestion.data;
				    	return;
				    },
				    width: 342
				});

				$('.login-trigger').click(function(){
	    			$(this).next('.login-form').slideToggle();
	            });
	        });
        </script>
        <a href="#" id="toTop"><span id="toTopHover"> </span></a>
    </body>
</html>
</#macro>