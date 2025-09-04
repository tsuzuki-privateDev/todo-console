enum Priority {
    HIGH(0), MEDIUM(1), LOW(2);
    final int order;
    Priority(int o) {
        this.order = o;
    }

    static Priority from(String s) {
        switch (s.toUpperCase()) {
            case "HIGH": return HIGH;
            case "MEDIUM": return MEDIUM;
            case "LOW": return LOW;
            default: throw new IllegalArgumentException("優先度は LOW / MEDIUM / HIGH のいずれか。");
        }
    }
}
