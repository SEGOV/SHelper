#!/bin/bash
# author sego1115

echo "Current folder is $(pwd)"

rm -frv ./boiler/compiled/*
rm -frv ./boiler/source/*
rm -frv ./boiler/processed_compiled/*
rm -frv ./boiler/processed_source/*
rm -frv ./applications/NetCracker/APP-INF/classes/com/netcracker/solutions/*
rm -frv ./applications/NetCracker/NetCrackerWebApp/WEB-INF/classes/com/netcracker/solutions/*

