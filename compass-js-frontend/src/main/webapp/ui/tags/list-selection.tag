<listselection>
	<div class="list-group">
		<a each="{list}" href="#" class="list-group-item" onclick={ parent.select }>{name}</a>
	</div>
	<script type="es6">
		var self = this;
		self.list = [];
		this.opts.list.each(function(s){
			var item = {
				name: s.name,
				id: s.id
			};
			self.list.push(item);
		});
		this.select = function(e){
			var el = $(e.currentTarget);
			el.addClass("active");
			el.siblings().removeClass("active");
			var id = e.item.id;
			self.opts.list.selectById(id);
		}
	</script>
</listselection>