package com.ersiver.newsster.utils


/**
 * A blocking version of NewssterRepository functions to minimize the number
 * of times I have to explicitly add <code>runBlocking { ... }</code> in the tests.
 */