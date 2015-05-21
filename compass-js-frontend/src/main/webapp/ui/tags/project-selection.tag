<projectselection>
	<div class="dropdown">
		<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
			Select a Project
			<span class="caret"></span>
		</button>
		<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
			<li each="{projects}" role="presentation">
				<a role="menuitem" tabindex="-1" href="#">{name}</a>
			</li>
		</ul>
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