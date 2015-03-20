<#import "/layout.ftl" as l>

<@l.page>

	<div class="header_bottom_left">
        <div class="categories">
           <ul>
               <h3>Genres</h3>
                  <li><a href="/">All</a></li>
                  <#list genres as genre>
                      <li><a href="/genre/${genre.id }">${genre.name}</a></li>
                  </#list>  
             </ul>
        </div>
     </div>
    <div class="header_bottom_right">
        <div class="content_top">
            <div class="heading">
            	<#if selectedGenre?has_content>
					<h3>${selectedGenre}</h3>
				<#elseif username?has_content>
					<h3>Recommended for you</h3>
				<#else>
					<h3>Random movies</h3>
				</#if>
            </div>
        </div>
        <div class="section group">
            <#list movies?chunk(5) as row>
            	<div>
            	<#list row as m>
	                <div class="grid_1_of_5 images_1_of_5">
	                    <a href="/movies/${m.id?c }"><img src="${m.poster!"/images/no-poster.jpg" }" alt="${m.title }" /></a>
	                    <div><a href="/movies/${m.id?c }">${m.title }</a></div>
	                </div>
                </#list>
                </div>
            </#list> 
            <br/>
        </div>
    </div>

</@l.page>