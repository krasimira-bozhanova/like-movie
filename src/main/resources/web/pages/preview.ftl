<#import "/layout.ftl" as l>

<@l.page>

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
                    <img src="${movie.poster }" alt="" />
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
                    <a href="${m.getId()}"><img src="${m.getImage()}" alt="" /></a>
                </div>
                
                <div class="movie_desc">
                    <h3><a href="${m.getId()}">${m.getTitle()}</a></h3>
                    <span><a href="#">Add to Cart</a></span>
                </div>
                <div class="clear"></div>
            </#list>
        </div>
    </div>

</@l.page>