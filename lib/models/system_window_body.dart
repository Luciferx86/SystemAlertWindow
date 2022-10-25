import 'package:system_alert_window/system_alert_window.dart';

class SystemWindowBody {
  List<EachRow>? rows;

  SystemWindowBody({this.rows});

  Map<String, dynamic> getMap() {
    final Map<String, dynamic> map = <String, dynamic>{
      'rows': (rows == null)
          ? null
          : List<dynamic>.from(rows!.map((x) => x.getMap())),
    };
    return map;
  }
}

class EachRow {
  List<EachColumn>? columns;

  EachRow(
      {this.columns});

  Map<String, dynamic> getMap() {
    final Map<String, dynamic> map = <String, dynamic>{
      'columns': (columns == null)
          ? null
          : List<dynamic>.from(columns!.map((x) => x.getMap())),
    };
    return map;
  }
}

class EachColumn {
  SystemWindowText? text;

  EachColumn({this.text});

  Map<String, dynamic> getMap() {
    final Map<String, dynamic> map = <String, dynamic>{
      'text': text?.getMap(),
    };
    return map;
  }
}
