<!DOCTYPE HTML>
    <head>
        <title>Free Movies Store Website Template | Home :: w3layouts</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
        <link href="/css/style.css" rel="stylesheet" type="text/css" media="all"/>
        <script type="text/javascript" src="/js/jquery-1.9.0.min.js"></script> 
        <script type="text/javascript" src="/js/move-top.js"></script>
        <script type="text/javascript" src="/js/easing.js"></script>
        <script type="text/javascript" src="/js/jquery.nivo.slider.js"></script>
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
                <div class="header_bottom">
                    <div class="header_bottom_left">
                        <div class="categories">
                           <ul>
                               <h3>Genres</h3>
                                  <li><a href="/">All</a></li>
                                  <#list genres as x>
                                      <li><a href="/genre/${x}">${x}</a></li>
                                  </#list>  
                             </ul>
                        </div>
                     </div>
                    <div class="header_bottom_right">
                        <div class="content_top">
                            <div class="heading">
                                <h3>Best rated</h3>
                            </div>
                        </div>
                        <div class="section group">
                            <#list movies as m>
                                <div class="grid_1_of_5 images_1_of_5">
                                    <a href="/movies/${m.getId()}"><img src="${m.getImage()}" alt="" /></a>
                                    <h2><a href="/movies/${m.getId()}">${m.getTitle()}</a></h2>
                                </div>
                            </#list> 
                        <br/>
                    </div>
                </div>
            </div>
        </div>
        
        <#include "preview.ftl" />

        <script type="text/javascript">
            $(document).ready(function() {
                $().UItoTop({ easingType: 'easeOutQuart' });
            });
        </script>
        <a href="#" id="toTop"><span id="toTopHover"> </span></a>
    </body>
</html>
