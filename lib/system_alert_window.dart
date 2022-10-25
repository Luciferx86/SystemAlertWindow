import 'dart:async';
import 'dart:convert';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:system_alert_window/models/system_window_body.dart';
import 'package:system_alert_window/utils/commons.dart';
import 'package:system_alert_window/utils/constants.dart';

export 'models/system_window_body.dart';
export 'models/system_window_text.dart';

enum SystemWindowPrefMode { DEFAULT, OVERLAY, BUBBLE }

class SystemAlertWindow {
  static const MethodChannel _channel = const MethodChannel(Constants.CHANNEL, JSONMethodCodec());

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String?> get getLogFile async {
    return await _channel.invokeMethod('getLogFile');
  }

  static Future<void> enableLogs(bool flag) async {
    await _channel.invokeMethod('enableLogs', [flag]);
  }

  static Future<bool?> checkPermissions({SystemWindowPrefMode prefMode = SystemWindowPrefMode.DEFAULT}) async {
    return await _channel.invokeMethod('checkPermissions', [Commons.getSystemWindowPrefMode(prefMode)]);
  }

  static Future<bool?> requestPermissions({SystemWindowPrefMode prefMode = SystemWindowPrefMode.DEFAULT}) async {
    return await _channel.invokeMethod('requestPermissions', [Commons.getSystemWindowPrefMode(prefMode)]);
  }

  static Future<bool> registerOnClickListener(Function callBackFunction) async {
    final callBackDispatcher = PluginUtilities.getCallbackHandle(callbackDispatcher);
    final callBack = PluginUtilities.getCallbackHandle(callBackFunction);

    _channel.setMethodCallHandler((MethodCall call) {
      print("Got callback");
      switch (call.method) {
        case "callBack":
          dynamic arguments = call.arguments;
          if (arguments is List) {
            final type = arguments[0];
            if (type == "onClick") {
              final tag = arguments[1];
              callBackFunction(tag);
            }
          }
      }
      return Future.value(null);
    });
    await _channel.invokeMethod("registerCallBackHandler", <dynamic>[callBackDispatcher!.toRawHandle(), callBack!.toRawHandle()]);
    return true;
  }

  static Future<bool?> showSystemWindow(
      {
      String notificationTitle = "Title",
      String notificationBody = "Body",
    SystemWindowPrefMode prefMode = SystemWindowPrefMode.DEFAULT,
    required Map<String, dynamic> extraData,
  }) async {
    return await _channel.invokeMethod(
      'showSystemWindow',
      [
        notificationTitle,
        notificationBody,
        extraData,
        Commons.getSystemWindowPrefMode(prefMode)
      ],
    );
  }

  static Future<bool?> closeSystemWindow({SystemWindowPrefMode prefMode = SystemWindowPrefMode.DEFAULT}) async {
    return await _channel.invokeMethod('closeSystemWindow', [Commons.getSystemWindowPrefMode(prefMode)]);
  }
}

void callbackDispatcher() {
  // 1. Initialize MethodChannel used to communicate with the platform portion of the plugin
  const MethodChannel _backgroundChannel = const MethodChannel(Constants.BACKGROUND_CHANNEL, JSONMethodCodec());
  // 2. Setup internal state needed for MethodChannels.
  WidgetsFlutterBinding.ensureInitialized();

  // 3. Listen for background events from the platform portion of the plugin.
  _backgroundChannel.setMethodCallHandler((MethodCall call) async {
    final args = call.arguments;
    // 3.1. Retrieve callback instance for handle.
    final Function callback = PluginUtilities.getCallbackFromHandle(CallbackHandle.fromRawHandle(args[0]))!;
    final type = args[1];
    if (type == "onClick") {
      final tag = args[2];
      // 3.2. Invoke callback.
      callback(tag);
    }
  });
}
