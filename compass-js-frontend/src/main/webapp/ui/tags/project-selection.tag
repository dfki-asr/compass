<projectselection>
	<div class="list-group">
		<a each="{projects}" href="#" class="list-group-item" onclick={ parent.select }>{name}</a>
	</div>
	<script type="es6">
		var self = this;
		self.projects = [];
		this.opts.projects.each(function(p){
			var item = {
				name: p.name
			};
			self.projects.push(item);
		});
	</script>
</projectselection>