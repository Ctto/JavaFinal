package creature;

public enum Color {
    RED("红色"), ORANGE("橙色"),YELLOW("黄色"),
    GREEN("绿色"), CYAN("青色"), BLUE("蓝色"), VIOLET("紫色");

    String CName;
    Color(String CName) {
        this.CName = CName;
    }
}
