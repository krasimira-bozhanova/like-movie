<#import "/layout.ftl" as l>

<@l.page>

    <div class="section group">
        <div class="cont-desc span_1_of_2">
            <div class="product-details">
                <div class="grid images_3_of_2">
                    <img src="${movie.poster!"/images/no-poster.jpg" }" alt="${movie.title }" />
                </div>
                <div class="desc span_3_of_2">
                    <h2>${movie.title }</h2>
                    <div class="available">
                        <ul>
                            <li><span>Year:</span> &nbsp; ${movie.year } </li>
                            <li><span>Duration:</span>&nbsp; ${movie.runtime }</li>
                            <li><span>Genre:</span>&nbsp; ${movie.genre }</li>
                            <li><span>Director:</span>&nbsp; ${movie.director }</li>
                            <li><span>Writer:</span>&nbsp; ${movie.writer }</li>
                            <li><span>Actors:</span>&nbsp; ${movie.actors }</li>
                            <li><span>Language:</span>&nbsp; ${movie.language }</li>
                            <li><span>Country:</span>&nbsp; ${movie.country }</li>
                            <li><span>IMDB rating:</span>&nbsp; ${movie.imdbRating }</li>
                        </ul>
                    </div>
                    <div class="share-desc">
                        <div class="rating">
                              <#if username?has_content>
                                  <#if liked>
                                     <a href="${movie.id?c}/unlike"><img width="48" src="/images/liked.png" alt="Unlike" title="Unlike"></img></a>
                                  <#else>
                                     <a href="${movie.id?c}/like"><img width="48" src="/images/liked.png" class="grayscale" alt="Like" title="Like"></img></a>
                                  </#if> 
                                  <#if watched>
                                     <a href="${movie.id?c}/unwatch"><img width="48" src="/images/watched.png" alt="Unwatch" title="Unwatch"></img></a> 
                                  <#else>
                                     <a href="${movie.id?c}/watch"><img width="48" src="/images/watched.png" class="grayscale" alt="Watch" title="Watch"></a>
                                  </#if> 
                              </#if>
                        </div>
                        <div class="clear"></div>
                    </div>
                </div>
                <div class="clear"></div>
            </div>
            <div class="product_desc">  
                <h2>Description :</h2>
                <p>${movie.plot }</p>
            </div>
        </div>
        <div class="rightsidebar span_3_of_1 sidebar">
            <h2>Specials</h2>
            <#list movies as m>
               <div class="special_movies">
                <div class="movie_poster">
                    <a href="${m.id?c }"><img src="${m.poster!"/images/no-poster.jpg" }" alt="" /></a>
                </div>
                
                <div class="movie_desc">
                    <h3><a href="${m.id?c }">${m.title }</a></h3>
                    <span>${m.shortenedPlot(70)}...</span>
                </div>
                <div class="clear"></div>
              </div>
            </#list>
        </div>
    </div>

</@l.page>