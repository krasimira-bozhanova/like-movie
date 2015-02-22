<!--A Design by W3layouts
Author: W3layout
Author URL: http://w3layouts.com
License: Creative Commons Attribution 3.0 Unported
License URL: http://creativecommons.org/licenses/by/3.0/
-->
<!DOCTYPE HTML>
    <head>
        <title>Free Movies Store Website Template | Preview :: w3layouts</title>
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
                    <div class="nav_list">
                        <ul>
                            <li><a href="index.html">${message}</a></li>
                            <li><a href="contact.html">Sitemap</a></li>
                            <li><a href="contact.html">Contact</a></li>
                        </ul>
                    </div>
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
                <div class="content_top">
                    <div class="back-links">
                    <p><a href="index.html">Home</a> &gt;&gt;&gt;&gt; <a href="#" class="active">English</a></p>
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="section group">
                    <div class="cont-desc span_1_of_2">
                        <div class="product-details">
                            <div class="grid images_3_of_2">
                                <img src="${movie.getImage()}" alt="" />
                            </div>
                            <div class="desc span_3_of_2">
                                <h2>${movie.getTitle()}</h2>
                                <div class="available">
                                    <ul>
                                        <li><span>Year:</span> &nbsp; ${movie.getYear()} </li>
                                        <li><span>Duration:</span>&nbsp; ${movie.getRuntime()}</li>
                                        <li><span>Genre:</span>&nbsp; ${movie.getGenre()}</li>
                                        <li><span>Director:</span>&nbsp; ${movie.getDirector()}</li>
                                        <li><span>Writer:</span>&nbsp; ${movie.getWriter()}</li>
                                        <li><span>Actors:</span>&nbsp; ${movie.getActors()}</li>
                                        <li><span>Language:</span>&nbsp; ${movie.getLanguage()}</li>
                                        <li><span>Country:</span>&nbsp; ${movie.getCountry()}</li>
                                        <li><span>IMDB rating:</span>&nbsp; ${movie.getImdbRating()}</li>
                                    </ul>
                                </div>
                                <div class="share-desc">
                                    <div class="rating">
                                        <div class="dropdown">
                                          <select name="vote">
                                            <option value="0" selected="selected" >Your rating:</option>
                                              <option value="1">1</option>
                                              <option value="2">2</option>
                                              <option value="3">3</option>
                                              <option value="4">4</option>
                                              <option value="5">5</option>
                                          </select>
                                          <input type="submit" value="Give rating" > 

                                        </div>
                                    </div>
                                    <div class="clear"></div>
                                </div>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="product_desc">  
                            <h2>Description :</h2>
                            <p>${movie.getPlot()}</p>
                        </div>
                    </div>
                    <div class="rightsidebar span_3_of_1 sidebar">
                        <h2>Specials</h2>
                        <#list movies as m>
                           <div class="special_movies">
                            <div class="movie_poster">
                                <a href="${m.getUrlTitle()}"><img src="${m.getImage()}" alt="" /></a>
                            </div>
                            
                            <div class="movie_desc">
                                <h3><a href="${m.getUrlTitle()}">${m.getTitle()}</a></h3>
                                <span><a href="#">Add to Cart</a></span>
                            </div>
                            <div class="clear"></div>
                        </#list>
                    </div>
                </div>
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

