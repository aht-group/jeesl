const searchField = document.querySelector('.sbSearch .ui-inputgroup input');
const cancelButton = document.querySelector('.sbSearch .ui-inputgroup').children[2];
const searchResults = document.querySelector('.sbSearch .auContainer'); 

searchField.addEventListener("click", expandSearch);
cancelButton.style.visibility = 'hidden';
searchField.style.width ='80px';
reloadSearch();

function reloadSearch() {
	if (searchField.value){
		cancelButton.style.visibility = 'visible';
	}
}

function cancelSearch() {
	console.log('cancelButton');
	searchField.value ='';
	searchResults.innerHTML = "";
	cancelButton.style.visibility = 'hidden';
	return false;
}
 

function collapsSearch() {
	searchField.setAttribute("style", "width:80px");
}
function expandSearch() {
	searchField.setAttribute("style", "width:160px");
}

function searchMin(){
 return searchField.value.length >= 1;
}

//	function showTipContent(obj){
//		var child = obj.children[0];
//		var width = child.children[0].offsetWidth;
//		console.log('offset-width : ' +width);
//
//		var customStyle = 'right:-20px; opacity:1; width:' + width + 'px; margin-right: -' + width +'px;';
//		console.log('Style : ' +customStyle);
//		child.setAttribute("style", customStyle);
//
//	}

//	function hideTipContent(obj){
//		var child = obj.children[0];
//		child.style.opacity = 0;
//	}

cancelButton.addEventListener ('click', function (evt) {
	cancelSearch();
	evt.preventDefault();
 });
		
    