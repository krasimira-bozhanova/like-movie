<#import "/layout.ftl" as l>

<@l.page>

	<div class="header_bottom_left">
        <div class="categories">
           <ul>
               <h3>Genres</h3>
                  <li><a href="/">All</a></li>
                  <#list genres as genre>
                      <li><a href="/genre/${genre.id }">${genre.name }</a></li>
                  </#list>  
             </ul>
        </div>
     </div>
    <div class="header_bottom_right">
        <div class="content_top">
            <div class="heading">
                <h3>Recommended (${selectedGenre})</h3>
            </div>
        </div>
        <div class="section group">
            <#list movies as m>
                <div class="grid_1_of_5 images_1_of_5">
                    <a href="/movies/${m.id }"><img src="${m.image }" alt="${m.title }" /></a>
                    <h2><a href="/movies/${m.id }">${m.title }</a></h2>
                </div>
            </#list> 
            <br/>
        </div>
    </div>

</@l.page>