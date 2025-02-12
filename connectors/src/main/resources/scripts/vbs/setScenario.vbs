'Initialize the needed objects: WEAP, File System, XML Processing
Set WEAP = CreateObject("WEAP.WEAPApplication") 
Set fso = CreateObject ("Scripting.FileSystemObject")
Set xmlDoc = CreateObject("Microsoft.XMLDOM") 

'Read the current temporary directory
Set tempDir = fso.GetSpecialFolder(2)

'Set streams to console and error
Set stdout = fso.GetStandardStream (1)
Set stderr = fso.GetStandardStream (2)

WEAP.ActiveScenario = WScript.Arguments(0)

stdout.WriteLine WEAP.ActiveScenario.Name