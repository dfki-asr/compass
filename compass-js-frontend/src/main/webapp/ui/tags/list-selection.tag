<listselection>
	<div class="list-group">
		<a each="{item in opts.collection.models}"
		   class="{list-group-item: true, active: parent.opts.collection.isSelected(item)}"
		   onclick="{parent.opts.clickHandler}">
			{item.name}
		</a>
	</div>
</listselection>