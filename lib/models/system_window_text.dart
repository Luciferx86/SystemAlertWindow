import 'package:flutter/material.dart';

class SystemWindowText {
  String text;
  double? fontSize;
  Color? textColor;

  SystemWindowText(
      {required this.text,
    this.fontSize,
    this.textColor,
  });

  Map<String, dynamic> getMap() {
    final Map<String, dynamic> map = <String, dynamic>{
      'text': text,
      'fontSize': fontSize ?? 14.0,
      'textColor':
          textColor?.value.toSigned(32) ?? Colors.black.value.toSigned(32),
    };
    return map;
  }
}
