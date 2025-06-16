public class ModeHandlerFactory {
    
    public static ModeHandler createHandler(Mode mode) {
        switch (mode) {
            case CODE:
                return new CodeModeHandler();
            case ARTICLE:
                return new ArticleModeHandler();
            case WORD:
                return new WordModeHandler();
            default:
                throw new IllegalArgumentException("Unsupported mode: " + mode);
        }
    }
}

