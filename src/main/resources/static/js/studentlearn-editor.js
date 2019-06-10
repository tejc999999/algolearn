var editor = ace.edit("editor");
editor.setTheme("ace/theme/monokai");
editor.session.setMode("ace/mode/javascript");
document.getElementById('editor').style.fontSize='12px';
var themeArray = [
	"chaos",
	"chrome",
	"clouds",
	"clouds_midnight",
	"cobalt",
	"crimson_editor",
	"down,dracula",
	"dreamweaver",
	"eclipse",
	"github",
	"gob",
	"gruvbox",
	"idle_finger",
	"iplastic",
	"katzenmilch",
	"kr_theme",
	"kuroir",
	"merbivore",
	"merbivore_soft",
	"mono_industrial",
	"monokai",
	"pestel_on_dark",
	"solarized_dark",
	"solarized_light",
	"sqlserver",
	"terminal",
	"textmeta",
	"tomorrow",
	"tomorrow_night",
	"tomorrow_night_blue",
	"tomorrow_night_bright",
	"tomorrow_night_eighties",
	"twilight",
	"vibrant_ink",
	"xcode"
	];
for(var i=0;i<themeArray.length;i++){
  let op = document.createElement("option");
  op.value = themeArray[i];  //value値
  op.text = themeArray[i];   //テキスト値
  document.getElementById("sel1").appendChild(op);
}
function changeFontSize($this) {
    document.getElementById('editor').style.fontSize= $this.value + 'px';
}
function changeTheme() {
    editor.setTheme("ace/theme/" + document.getElementById("sel1").value);
}