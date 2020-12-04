package DatabaseSystem;

public interface Savable {

    public String getCollectionName();

    public ConversionStrategy getConversionStrategy();

    public ParserStrategy getDocumentParserStrategy();

}