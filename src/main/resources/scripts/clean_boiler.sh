#!/bin/bash
# author segov

echo "Current folder is $(pwd)"

rm -frv ./boiler/compiled/*
rm -frv ./boiler/source/*
rm -frv ./boiler/processed_compiled/*
rm -frv ./boiler/processed_source/*
rm -frv ./applications/***/APP-INF/classes/com/***/solutions/*
rm -frv ./applications/***/***/WEB-INF/classes/com/***/solutions/*

