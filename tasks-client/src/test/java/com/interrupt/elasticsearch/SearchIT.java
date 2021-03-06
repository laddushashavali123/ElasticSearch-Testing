package com.interrupt.elasticsearch;

import com.google.gson.JsonObject;
import com.interrupt.elasticsearch.commands.DeleteCommand;
import com.interrupt.elasticsearch.commands.IndexCommand;
import com.interrupt.elasticsearch.commands.SearchCommand;
import com.interrupt.tasks.model.Task;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SearchIT {

    @Test
    public void shouldIndexTask() {
        IndexCommand command = new IndexCommand("testkey", new Task("Test Search Title", "Test Search Body 2"));
        Boolean result = command.execute();
        assertEquals(true, result);
    }

    @Test
    public void shouldExecuteSearch() {
        JsonObject found = new SearchCommand("Search").execute();
        assertNotNull(found);
    }

    @Test
    public void shouldDeleteIndex() throws Exception {
        DeleteCommand command = new DeleteCommand("testkey");
        Boolean result = command.execute();
        assertEquals(true, result);
    }
}
