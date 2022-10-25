import 'package:system_alert_window/system_alert_window.dart';

class Commons {


  static String getSystemWindowPrefMode(SystemWindowPrefMode prefMode) {
    //if (prefMode == null) prefMode = SystemWindowPrefMode.DEFAULT;
    switch (prefMode) {
      case SystemWindowPrefMode.OVERLAY:
        return "overlay";
      case SystemWindowPrefMode.BUBBLE:
        return "bubble";
      case SystemWindowPrefMode.DEFAULT:
      default:
        return "default";
    }
  }
}
