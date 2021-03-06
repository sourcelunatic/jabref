package org.jabref.gui.autocompleter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.jabref.model.entry.BibEntry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.jabref.gui.autocompleter.AutoCompleterUtil.getRequest;

public class FieldValueSuggestionProviderTest {
    private FieldValueSuggestionProvider autoCompleter;

    @Before
    public void setUp() throws Exception {
        autoCompleter = new FieldValueSuggestionProvider("field");
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void initAutoCompleterWithNullFieldThrowsException() {
        new FieldValueSuggestionProvider(null);
    }

    @Test
    public void completeWithoutAddingAnythingReturnsNothing() {
        Collection<String> result = autoCompleter.call(getRequest(("test")));
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void completeAfterAddingNullReturnsNothing() {
        autoCompleter.indexEntry(null);

        Collection<String> result = autoCompleter.call(getRequest(("test")));
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void completeAfterAddingEmptyEntryReturnsNothing() {
        BibEntry entry = new BibEntry();
        autoCompleter.indexEntry(entry);

        Collection<String> result = autoCompleter.call(getRequest(("test")));
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void completeAfterAddingEntryWithoutFieldReturnsNothing() {
        BibEntry entry = new BibEntry();
        entry.setField("title", "testTitle");
        autoCompleter.indexEntry(entry);

        Collection<String> result = autoCompleter.call(getRequest(("test")));
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void completeValueReturnsValue() {
        BibEntry entry = new BibEntry();
        entry.setField("field", "testValue");
        autoCompleter.indexEntry(entry);

        Collection<String> result = autoCompleter.call(getRequest(("testValue")));
        Assert.assertEquals(Arrays.asList("testValue"), result);
    }

    @Test
    public void completeBeginnigOfValueReturnsValue() {
        BibEntry entry = new BibEntry();
        entry.setField("field", "testValue");
        autoCompleter.indexEntry(entry);

        Collection<String> result = autoCompleter.call(getRequest(("test")));
        Assert.assertEquals(Arrays.asList("testValue"), result);
    }

    @Test
    public void completeLowercaseValueReturnsValue() {
        BibEntry entry = new BibEntry();
        entry.setField("field", "testValue");
        autoCompleter.indexEntry(entry);

        Collection<String> result = autoCompleter.call(getRequest(("testvalue")));
        Assert.assertEquals(Arrays.asList("testValue"), result);
    }

    @Test(expected = NullPointerException.class)
    public void completeNullThrowsException() {
        BibEntry entry = new BibEntry();
        entry.setField("field", "testKey");
        autoCompleter.indexEntry(entry);

        autoCompleter.call(getRequest((null)));
    }

    @Test
    public void completeEmptyStringReturnsNothing() {
        BibEntry entry = new BibEntry();
        entry.setField("field", "testKey");
        autoCompleter.indexEntry(entry);

        Collection<String> result = autoCompleter.call(getRequest(("")));
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void completeReturnsMultipleResults() {
        BibEntry entryOne = new BibEntry();
        entryOne.setField("field", "testValueOne");
        autoCompleter.indexEntry(entryOne);
        BibEntry entryTwo = new BibEntry();
        entryTwo.setField("field", "testValueTwo");
        autoCompleter.indexEntry(entryTwo);

        Collection<String> result = autoCompleter.call(getRequest(("testValue")));
        Assert.assertEquals(Arrays.asList("testValueOne", "testValueTwo"), result);
    }

    @Test
    public void completeShortStringReturnsFieldValue() {
        BibEntry entry = new BibEntry();
        entry.setField("field", "val");
        autoCompleter.indexEntry(entry);

        Collection<String> result = autoCompleter.call(getRequest(("va")));
        Assert.assertEquals(Collections.singletonList("val"), result);
    }

    @Test
    public void completeBeginnigOfSecondWordReturnsWholeFieldValue() {
        BibEntry entry = new BibEntry();
        entry.setField("field", "test value");
        autoCompleter.indexEntry(entry);

        Collection<String> result = autoCompleter.call(getRequest(("val")));
        Assert.assertEquals(Collections.singletonList("test value"), result);
    }

    @Test
    public void completePartOfWordReturnsWholeFieldValue() {
        BibEntry entry = new BibEntry();
        entry.setField("field", "test value");
        autoCompleter.indexEntry(entry);

        Collection<String> result = autoCompleter.call(getRequest(("lue")));
        Assert.assertEquals(Collections.singletonList("test value"), result);
    }

    @Test
    public void completeReturnsWholeFieldValue() {
        BibEntry entry = new BibEntry();
        entry.setField("field", "test value");
        autoCompleter.indexEntry(entry);

        Collection<String> result = autoCompleter.call(getRequest(("te")));
        Assert.assertEquals(Collections.singletonList("test value"), result);
    }
}
