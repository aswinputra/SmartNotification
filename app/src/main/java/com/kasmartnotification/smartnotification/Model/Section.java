package com.kasmartnotification.smartnotification.Model;

/**
 * Section object for SectionedRecyclerViewAdapter
 */
public class Section {
    int firstPosition;
    int sectionedPosition;
    CharSequence title;

    public Section(int firstPosition, CharSequence title, int sectionedPosition) {
        this.firstPosition = firstPosition;
        this.title = title;
        this.sectionedPosition = sectionedPosition;
    }

    public CharSequence getTitle() {
        return title;
    }

    public int getFirstPosition() {
        return firstPosition;
    }

    public void setFirstPosition(int firstPosition) {
        this.firstPosition = firstPosition;
    }

    public int getSectionedPosition() {
        return sectionedPosition;
    }

    public void setSectionedPosition(int sectionedPosition) {
        this.sectionedPosition = sectionedPosition;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public boolean is(String title){
        return this.title.equals(title);
    }

    public int getIndexInRecyclerView(){
        return firstPosition + sectionedPosition;
    }
}