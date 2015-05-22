<projectselection>
	<div class="list-group">
		<a each="{projects}" href="#" class="list-group-item" onclick={ parent.select }>{name}</a>
	</div>
	<script type="es6">
		var self = this;
		self.projects = [];
		this.opts.projects.each(function(p){
			var item = {
				name: p.name,
				id: p.id
			};
			self.projects.push(item);
		});
		this.select = function(e){
			var el = $(e.currentTarget);
			el.addClass("active");
			el.siblings().removeClass("active");
			var id = e.item.id;
			self.opts.projects.selectById(id);
		}
	</script>
</projectselection>