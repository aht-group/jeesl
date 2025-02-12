'Initialize the needed objects: WEAP, File System, XML Processing
Set WEAP = CreateObject("WEAP.WEAPApplication") 
Set fso = CreateObject ("Scripting.FileSystemObject")
Set xmlDoc = CreateObject("Microsoft.XMLDOM") 

'Read the current temporary directory
Set tempDir = fso.GetSpecialFolder(2)

'Set streams to console and error
Set stdout = fso.GetStandardStream (1)
Set stderr = fso.GetStandardStream (2)

'Test console logging
'stdout.WriteLine "Hello, " & WScript.Arguments(0) & ". WEAP is currently located in " & WEAP.Directory
'stdout.WriteLine tempDir

'WEAP.ActiveArea ="WEAPversion02"
WEAP.Verbose = 0
IF WScript.Arguments(3) = "NOTSET" Then
    stdout.WriteLine WEAP.ResultValue(WScript.Arguments(0) & ":" & WScript.Arguments(1) & "[" & WScript.Arguments(2) & "]",WScript.Arguments(4),WScript.Arguments(5),WScript.Arguments(6),WScript.Arguments(7),WScript.Arguments(8))
ELSE
    stdout.WriteLine WEAP.ResultValue(WScript.Arguments(0) & ":" & WScript.Arguments(1) & "[" & WScript.Arguments(2) & ", " & WScript.Arguments(3) & "]",WScript.Arguments(4),WScript.Arguments(5),WScript.Arguments(6),WScript.Arguments(7),WScript.Arguments(8))
END IF