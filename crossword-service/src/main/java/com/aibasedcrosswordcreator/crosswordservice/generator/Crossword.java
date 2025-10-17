package com.aibasedcrosswordcreator.crosswordservice.generator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class Crossword {
    private CrosswordCell[][] body;
    private String theme;
    private List<Word> words = new ArrayList<>();
    private String language;

    public Crossword(CrosswordCell[][] body, String theme) {
        this.body = body;
        this.theme = theme;
    }

    public String[][] bodyToString() {
        String[][] body = new String[this.body.length][this.body[0].length];
        for (int i = 0; i < this.body.length; i++) {
            for (int j = 0; j < this.body[0].length; j++) {
                body[i][j] = this.body[i][j].getContent();
            }
        }
        return body;
    }

    public void printBody() {
        for (int i = 0; i < this.body[0].length + 2; i++) {
            System.out.print("--");
        }
        System.out.print("\n");
        for (int i = 0; i < this.getBody().length; i++) {
            System.out.print("|");
            for (int j = 0; j < this.getBody()[0].length; j++) {
                if (this.getBody()[i][j].getContent() == null) {
                    System.out.print("  ");
                    continue;
                }
                System.out.print(this.getBody()[i][j].getContent() + " ");
            }
            System.out.print("|\n");
        }
        for (int i = 0; i < this.body[0].length + 2; i++) {
            System.out.print("--");
        }
        System.out.print("\n");
    }

    public void placeWordHorizontally(String word, int wordIndex, Point startPoint) {
        if (startPoint.wIndex() + word.length() - 1 > body[0].length - 1 || startPoint.wIndex() - 1 < 0) {
            throw new CannotPlaceWordException("Does not fit in the body horizontally");
        }
        if (this.body[startPoint.hIndex()][startPoint.wIndex() - 1].getContent() != null) {
            throw new CannotPlaceWordException("Word covers other word horizontally");
        }
        if (startPoint.wIndex() + word.length() < this.body.length && this.body[startPoint.hIndex()][startPoint.wIndex() + word.length()].getContent() != null && !this.body[startPoint.hIndex()][startPoint.wIndex() + word.length()].isWordIdentifier) {
            throw new CannotPlaceWordException("No space between words");
        }
        for (int i = 0; i < word.length(); i++) {
            if (this.body[startPoint.hIndex()][startPoint.wIndex() + i].getContent() != null && !this.body[startPoint.hIndex()][startPoint.wIndex() + i].getContent().equals(String.valueOf(word.charAt(i)))) {
                throw new CannotPlaceWordException("Word covers other word horizontally");
            }
        }
        for (int i = 0; i < word.length(); i++) {
            this.body[startPoint.hIndex()][startPoint.wIndex() + i].setContent(String.valueOf(word.charAt(i)));
        }
        var newWord = new Word(WordOrientation.HORIZONTAL, startPoint, word, String.valueOf(wordIndex));
        this.body[startPoint.hIndex()][startPoint.wIndex() - 1].setContent(String.valueOf(wordIndex));
        this.body[startPoint.hIndex()][startPoint.wIndex() - 1].setWordIdentifier(true);
        this.body[startPoint.hIndex()][startPoint.wIndex() - 1].setOwner(newWord);
        this.words.add(newWord);
    }

    public void placeWordVertically(String word, int wordIndex, Point startPoint) {
        if (startPoint.hIndex() + word.length() - 1 > body.length - 1 || startPoint.hIndex() - 1 < 0) {
            throw new CannotPlaceWordException("Does not fit in the body vertically");
        }
        if (this.body[startPoint.hIndex() - 1][startPoint.wIndex()].getContent() != null) {
            throw new CannotPlaceWordException("Word covers other word vertically");
        }
        if (startPoint.hIndex() + word.length() < this.body.length && this.body[startPoint.hIndex() + word.length()][startPoint.wIndex()].getContent() != null && !this.body[startPoint.hIndex() + word.length()][startPoint.wIndex()].isWordIdentifier) {
            throw new CannotPlaceWordException("No space between words");
        }
        for (int i = 0; i < word.length(); i++) {
            if (this.body[startPoint.hIndex() + i][startPoint.wIndex()].getContent() != null && !this.body[startPoint.hIndex() + i][startPoint.wIndex()].getContent().equals(String.valueOf(word.charAt(i)))) {
                throw new CannotPlaceWordException("Word covers other word vertically");
            }
        }
        for (int i = 0; i < word.length(); i++) {
            this.body[startPoint.hIndex() + i][startPoint.wIndex()].setContent(String.valueOf(word.charAt(i)));
        }
        var newWord = new Word(WordOrientation.VERTICAL, startPoint, word, String.valueOf(wordIndex));
        this.body[startPoint.hIndex() - 1][startPoint.wIndex()].setContent(String.valueOf(wordIndex));
        this.body[startPoint.hIndex() - 1][startPoint.wIndex()].setWordIdentifier(true);
        this.body[startPoint.hIndex() - 1][startPoint.wIndex()].setOwner(newWord);
        this.words.add(newWord);
    }

    public boolean isSpaceNextToWord(String word, Point startPoint, WordOrientation orientation) {
        if (orientation == WordOrientation.HORIZONTAL) {
            if (startPoint.wIndex() + word.length() - 1 >= body[0].length) {
                throw new CannotPlaceWordException("Does not fit in the body vertically");
            }
            if ((startPoint.wIndex() + word.length() < body[0].length) && !this.body[startPoint.hIndex()][startPoint.wIndex() + word.length()].isWordIdentifier() && this.body[startPoint.hIndex()][startPoint.wIndex() + word.length()].getContent() != null) {
                return false;
            }
            // checks all the characters to end
            for (int i = 0; i < word.length(); i++) {
                if (this.body[startPoint.hIndex()][startPoint.wIndex() + i].getContent() != null) {
                    continue;
                }
                if (startPoint.hIndex() - 1 >= 0 && !this.body[startPoint.hIndex() - 1][startPoint.wIndex() + i].isWordIdentifier() && this.body[startPoint.hIndex() - 1][startPoint.wIndex() + i].getContent() != null) {
                    throw new CannotPlaceWordException("Placed word next to upper word");
                    //return false;
                }
                if (startPoint.hIndex() + 1 < this.body.length && !this.body[startPoint.hIndex() + 1][startPoint.wIndex() + i].isWordIdentifier() && this.body[startPoint.hIndex() + 1][startPoint.wIndex() + i].getContent() != null) {
                    throw new CannotPlaceWordException("Placed word next to bottom word");
                    //return false;
                }
            }
        }
        if (orientation == WordOrientation.VERTICAL) {
            if (startPoint.hIndex() + word.length() - 1 >= body.length) {
                throw new CannotPlaceWordException("Does not fit in the body vertically");
            }
            if ((startPoint.hIndex() + word.length() < body.length) && !this.body[startPoint.hIndex() + word.length()][startPoint.wIndex()].isWordIdentifier() && this.body[startPoint.hIndex() + word.length()][startPoint.wIndex()].getContent() != null) {
                return false;
            }
            for (int i = 0; i < word.length(); i++) {
                if (this.body[startPoint.hIndex() + i][startPoint.wIndex()].getContent() != null) {
                    //System.out.println("word=" + word + "\t" + startPoint);
                    continue;
                }
                if (startPoint.wIndex() - 1 >= 0 && !this.body[startPoint.hIndex() + i][startPoint.wIndex() - 1].isWordIdentifier() && this.body[startPoint.hIndex() + i][startPoint.wIndex() - 1].getContent() != null) {
                    throw new CannotPlaceWordException("Placed word next to left word");
                    //return false;
                }
                if (startPoint.wIndex() + 1 < this.body[0].length && !this.body[startPoint.hIndex() + i][startPoint.wIndex() + 1].isWordIdentifier() && this.body[startPoint.hIndex() + i][startPoint.wIndex() + 1].getContent() != null) {
                    throw new CannotPlaceWordException("Placed word next to right word");
                    //return false;
                }
            }
        }
        return true;
    }

    public void findPlaceAndPutWord(String wordToPut, boolean isSpaceNextToWord) {
        if (words.isEmpty()) {
            throw new CannotPlaceWordException("This method does not work when the crossword body is empty");
        }
        // go through all the words
        for (Word word : words) {
            // checks if word to put can be placed crosses the checked word
            for (int i = 0; i < word.content.length(); i++) {
                // checks if word to put crosses the specified character
                int index = wordToPut.indexOf(word.content.charAt(i));
                while (index != -1) {
                    try {
                        if (isSpaceNextToWord) {
                            // getting start index of word to put and trying to place
                            //System.out.println("point=" + new Point(word.startPoint.hIndex() + i, word.startPoint.wIndex() - index) + "\t i=" + i + "word orient=" + word.orientation + "\t index=" + index + "\t word=" + word.content + "\t" + word.startPoint);
                            if (word.startPoint.wIndex() - index >= 0 && word.orientation == WordOrientation.VERTICAL && this.isSpaceNextToWord(wordToPut, new Point(word.startPoint.hIndex() + i, word.startPoint.wIndex() - index), WordOrientation.HORIZONTAL)) {
                                //System.out.println("H");
                                this.placeWordHorizontally(wordToPut, words.size() + 1, new Point(word.startPoint.hIndex() + i, word.startPoint.wIndex() - index));
                                return;
                            }
                            else if (word.startPoint.hIndex() - index >= 0 && word.orientation == WordOrientation.HORIZONTAL && this.isSpaceNextToWord(wordToPut, new Point(word.startPoint.hIndex() - index, word.startPoint.wIndex() + i), WordOrientation.VERTICAL)) {
                                //System.out.println("V");
                                this.placeWordVertically(wordToPut, words.size() + 1, new Point(word.startPoint.hIndex() - index, word.startPoint.wIndex() + i));
                                return;
                            }
                        }
                        else {
                            if (word.orientation == WordOrientation.VERTICAL) {
                                //System.out.println("point=" + new Point(word.startPoint.hIndex() + i, word.startPoint.wIndex() - index) + "\ti=" + i + "\thorizontally   word=" + wordToPut + "\t index=" + index);
                                this.placeWordHorizontally(wordToPut, words.size() + 1, new Point(word.startPoint.hIndex() + i, word.startPoint.wIndex() - index));
                                return;
                            }
                            else if (word.orientation == WordOrientation.HORIZONTAL) {
                                //System.out.println("point=" + new Point(word.startPoint.hIndex() - index, word.startPoint.wIndex() + i) + "\ti=" + i + "\tvertically   word=" + wordToPut + "\t index=" + index);
                                this.placeWordVertically(wordToPut, words.size() + 1, new Point(word.startPoint.hIndex() - index, word.startPoint.wIndex() + i));
                                return;
                            }
                        }
                    } catch (CannotPlaceWordException e) {
                        //System.out.println(e.getMessage());
                        // if word to put cannot be placed then it continues checking the next character
                    }
                    index = wordToPut.indexOf(word.content.charAt(i), index + 1);
                }
            }
            // goes to the next word
        }
        throw new CannotPlaceWordException("Word cannot be placed in the crossword body");
    }

    public boolean checkAllSides() {
        boolean upper = false;
        boolean bottom = false;
        boolean left = false;
        boolean right = false;

        for (int i = 0; i < body[0].length; i++) {
            if (body[0][i].getContent() != null) {
                upper = true;
            }
            if (body[body.length - 1][i].getContent() != null) {
                bottom = true;
            }
        }

        for (CrosswordCell[] crosswordCells : body) {
            if (crosswordCells[0].getContent() != null) {
                left = true;
            }
            if (crosswordCells[crosswordCells.length - 1].getContent() != null) {
                right = true;
            }
        }

        return upper && bottom && left && right;
    }

}
