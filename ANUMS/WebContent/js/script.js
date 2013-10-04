// Advanced search toggle.
	window.onload = function() {
		var fbAdvanceToggle = document.getElementById('fb-advanced-toggle');
		if (fbAdvanceToggle) {
			fbAdvanceToggle.onclick = function() {
				var advancedSearch = document.getElementById('fb-advanced');
				var simpleSearch = document.getElementById('fb-queryform');
				this.innerHTML = (this.innerHTML != 'Advanced Search' ? 'Advanced Search'
						: 'Simple Search');
				advancedSearch.style.display = (advancedSearch.style.display != 'none' ? 'none'
						: '');
				simpleSearch.style.display = (simpleSearch.style.display != 'none' ? 'none'
						: '');
				return false;
			};
		}
	};