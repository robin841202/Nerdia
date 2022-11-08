package com.example.movieinfo.model;

/**
 * Data that used in SlideShowAdapter
 */
public class SlideShowItemData {

    /**
     * Item type define in StaticParameter.SlideShowType (VIDEO,IMAGE)
     */
    private int itemType;

    /**
     * Item source, VIDEO: VideoData.videoId ; IMAGE: ImageData.filePath
     */
    private String source;

    /**
     * Only got value when itemType=IMAGE; ex: w1280
     */
    private String imageSizeType;

    public SlideShowItemData(int itemType, String source) {
        this.itemType = itemType;
        this.source = source;
    }
    public SlideShowItemData(int itemType, String source, String imageSizeType) {
        this.itemType = itemType;
        this.source = source;
        this.imageSizeType = imageSizeType;
    }

    public int getItemType() {
        return itemType;
    }

    public String getSource() {
        return source;
    }

    public String getImageSizeType() {
        return imageSizeType;
    }
}