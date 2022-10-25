import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:system_alert_window/system_alert_window.dart';
import 'package:share_plus/share_plus.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(MyApp());
}

///
/// Whenever a button is clicked, this method will be invoked with a tag (As tag is unique for every button, it helps in identifying the button).
/// You can check for the tag value and perform the relevant action for the button click
///
void callBack(String tag) {
  WidgetsFlutterBinding.ensureInitialized();
  print(tag);
  switch (tag) {
    case "simple_button":
    case "updated_simple_button":
      SystemAlertWindow.closeSystemWindow(prefMode: SystemWindowPrefMode.OVERLAY);
      break;
    case "focus_button":
      print("Focus button has been called");
      break;
    default:
      print("OnClick event of $tag");
  }
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  bool _isShowingWindow = false;
  SystemWindowPrefMode prefMode = SystemWindowPrefMode.OVERLAY;

  @override
  void initState() {
    super.initState();
    _initPlatformState();
    _requestPermissions();
    SystemAlertWindow.registerOnClickListener(callBack);
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> _initPlatformState() async {
    await SystemAlertWindow.enableLogs(true);
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await SystemAlertWindow.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> _requestPermissions() async {
    await SystemAlertWindow.requestPermissions(prefMode: prefMode);
  }

  void _showOverlayWindow() {
    if (!_isShowingWindow) {
      SystemWindowBody body = SystemWindowBody(
        rows: [
          EachRow(
            columns: [
              EachColumn(
                text: SystemWindowText(text: "Some body", fontSize: 12, textColor: Colors.black45),
              ),
            ],
          ),
          EachRow(columns: [
            EachColumn(
                text: SystemWindowText(
                  text: "Long data of the body",
                  fontSize: 12,
                  textColor: Colors.black87,
                ),
              ),
            ],
          ),
          EachRow(
            columns: [
              EachColumn(
                text: SystemWindowText(text: "Notes", fontSize: 10, textColor: Colors.black45),
              ),
            ],
          ),
          EachRow(
            columns: [
              EachColumn(
                text: SystemWindowText(
                  text: "Some random notes.",
                  fontSize: 13,
                  textColor: Colors.black54,
                ),
              ),
            ],
          ),
        ],
      );
      SystemAlertWindow.showSystemWindow(
        body: body,
        notificationTitle: "Incoming Call",
          notificationBody: "+1 646 980 4741",
        prefMode: prefMode,
      );
      setState(() {
        _isShowingWindow = true;
      });
    } else {
      setState(() {
        _isShowingWindow = false;
      });
      SystemAlertWindow.closeSystemWindow(prefMode: prefMode);
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('System Alert Window Example App'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              Text('Running on: $_platformVersion\n'),
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 8.0),
                child: MaterialButton(
                  onPressed: _showOverlayWindow,
                  textColor: Colors.white,
                  child: !_isShowingWindow
                      ? Text("Show system alert window")
                      : Text("Close system alert window"),
                          
                  color: Colors.deepOrange,
                  padding: const EdgeInsets.symmetric(vertical: 8.0),
                ),
              ),
              TextButton(onPressed: () async{
                String logFilePath = await SystemAlertWindow.getLogFile;
                if(logFilePath != null && logFilePath.isNotEmpty) {
                  Share.shareFiles([logFilePath]);
                }else{
                  print("Path is empty");
                }
              }, child: Text("Share Log file"))
            ],
          ),
        ),
      ),
    );
  }
}
